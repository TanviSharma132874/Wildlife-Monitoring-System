import cv2
import time
import pygame
import os
import numpy as np

from ultralytics import YOLO

from config import *
from embedding_engine import generate_embedding
from matcher import find_best_match
from utils import save_crop
from api_client import send_detection

print("🌿 Wildlife Monitoring Started")

# =========================
# 🔊 SOUND SETUP
# =========================
high_siren = None
medium_siren = None

try:
    pygame.mixer.init()
    BASE_DIR = os.path.dirname(__file__)

    HIGH_ALERT_SOUND = os.path.join(BASE_DIR, "sounds", "siren.mp3")
    MEDIUM_ALERT_SOUND = os.path.join(BASE_DIR, "sounds", "warning.mp3")

    if os.path.exists(HIGH_ALERT_SOUND) and os.path.exists(MEDIUM_ALERT_SOUND):
        high_siren = pygame.mixer.Sound(HIGH_ALERT_SOUND)
        medium_siren = pygame.mixer.Sound(MEDIUM_ALERT_SOUND)
        print("🔊 Sounds Loaded")

except Exception as e:
    print("⚠ Sound Error:", e)

# =========================
# MODEL
# =========================
model = YOLO(MODEL_PATH)

# Debug classes
print("📊 Model Classes:", model.names)

cap = cv2.VideoCapture(CAMERA_INDEX, cv2.CAP_DSHOW)

if not cap.isOpened():
    print("❌ Camera not opening")
    exit()

last_time = 0

LAST_DETECTION = {
    "animal_id": None,
    "embedding": None,
    "time": 0
}

DETECTION_COOLDOWN = 6
MEMORY_TIME = 5

# =========================
# MAIN LOOP
# =========================
while True:

    ret, frame = cap.read()
    if not ret:
        break

    current_time = time.time()

    if current_time - last_time < COOLDOWN_SECONDS:
        cv2.imshow("Wildlife Monitor", frame)
        if cv2.waitKey(1) == 27:
            break
        continue

    # 🔥 FIXED PREDICT CALL
    results = model.predict(frame, conf=CONF_THRESHOLD, verbose=False)

    for r in results:

        if r.boxes is None:
            continue

        for box in r.boxes:

            cls = int(box.cls[0])
            species_name = model.names[cls].lower()

            if species_name not in SPECIES_MAP:
                continue

            species_id = SPECIES_MAP[species_name]
            confidence = float(box.conf[0])

            print(f"\n📸 {species_name} | {confidence:.2f}")

            # =========================
            # SAVE IMAGE
            # =========================
            path, filename = save_crop(frame, box.xyxy[0], species_name)

            if not path:
                continue

            # =========================
            # SOUND
            # =========================
            if confidence >= 0.7 and high_siren:
                high_siren.play()
            elif confidence >= 0.4 and medium_siren:
                medium_siren.play()

            # =========================
            # EMBEDDING
            # =========================
            embedding = None
            animal_id = None

            try:
                embedding = generate_embedding(path)

                if embedding is not None:

                    # Memory match
                    if LAST_DETECTION["embedding"] is not None:
                        score_mem = np.dot(embedding, LAST_DETECTION["embedding"])

                        if score_mem > 0.75 and (current_time - LAST_DETECTION["time"] < MEMORY_TIME):
                            animal_id = LAST_DETECTION["animal_id"]
                            print("⚡ Memory match used:", animal_id)

                    # DB match
                    if animal_id is None:
                        animal_id, score = find_best_match(embedding, species_id)

                        print(f"🔍 DB Score: {score:.3f}")

                        if animal_id:
                            print(f"🎯 Matched: {animal_id}")
                        else:
                            print("⚠ Unknown animal")

            except Exception as e:
                print("⚠ Embedding Error:", e)

            # Duplicate control
            now = time.time()

            if animal_id and LAST_DETECTION["animal_id"] == animal_id:
                if now - LAST_DETECTION["time"] < DETECTION_COOLDOWN:
                    print("⏳ Duplicate skipped")
                    continue

            # Update memory
            LAST_DETECTION["animal_id"] = animal_id
            LAST_DETECTION["embedding"] = embedding
            LAST_DETECTION["time"] = now

            # Send to server
            data = {
                "camera_id": 1,
                "species_id": species_id,
                "confidence": confidence,
                "image_name": filename,
                "location": "Zone A"
            }

            if embedding is not None:
                data["embedding"] = embedding.tolist()

            if animal_id:
                data["animal_id"] = animal_id

            send_detection(data)
            print("✅ Sent to server")

            last_time = current_time

    cv2.imshow("Wildlife Monitor", frame)

    if cv2.waitKey(1) == 27:
        break

cap.release()
cv2.destroyAllWindows()
pygame.mixer.quit()