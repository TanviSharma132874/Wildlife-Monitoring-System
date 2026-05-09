import os
import cv2
import time
from config import DETECTION_DIR

def save_crop(frame, bbox, species_name):

    try:
        x1, y1, x2, y2 = map(int, bbox)

        h, w, _ = frame.shape

        # 🔥 SAFE BOUNDARY FIX
        x1 = max(0, x1)
        y1 = max(0, y1)
        x2 = min(w, x2)
        y2 = min(h, y2)

        crop = frame[y1:y2, x1:x2]

        if crop.size == 0:
            print("⚠ Empty crop")
            return None, None

        # 🔥 UNIQUE NAME
        filename = f"{species_name}_{int(time.time()*1000)}.jpg"

        full_path = os.path.join(DETECTION_DIR, filename)

        cv2.imwrite(full_path, crop)

        print("✅ Saved:", full_path)

        return full_path, filename

    except Exception as e:
        print("❌ Crop Error:", e)
        return None, None