import os

BASE_DIR = os.path.dirname(__file__)

CAMERA_INDEX = 0

MODEL_PATH = os.path.join(BASE_DIR, "best.pt")

CONF_THRESHOLD = 0.4
COOLDOWN_SECONDS = 4

# 🔥 FIXED (was 0.7)
SIMILARITY_THRESHOLD = 0.65

SERVER_URL = "http://localhost:8081/WildlifeMonitoringWeb/DetectionServlet"

STORAGE_BASE = "C:/wildlife_storage"

DETECTION_DIR = os.path.join(STORAGE_BASE, "detections")
ANIMAL_DIR = os.path.join(STORAGE_BASE, "animals")

os.makedirs(DETECTION_DIR, exist_ok=True)
os.makedirs(ANIMAL_DIR, exist_ok=True)

SAVE_DIR = DETECTION_DIR

DB_CONFIG = {
    "host": "127.0.0.1",
    "user": "root",
    "password": "",
    "database": "wildlife_monitoring",
    "port": 3306
}

SPECIES_MAP = {
    "tiger": 1,
    "elephant": 2,
    "leopard": 3,
    "deer": 4,
    "lion": 5,
    "bear": 6,
    "monkey": 7,
    "rhino": 8,
    "zebra": 9,
    "hippopotamus": 10,
    "pig": 11,
    "giraffe": 12
}