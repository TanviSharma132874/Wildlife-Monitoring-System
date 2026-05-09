import requests
from config import SERVER_URL

def send_detection(data):
    try:
        res = requests.post(SERVER_URL, json=data)
        return res.text
    except Exception as e:
        print("❌ API Error:", e)