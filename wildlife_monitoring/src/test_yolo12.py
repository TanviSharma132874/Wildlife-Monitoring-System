import cv2
from ultralytics import YOLO

print("YOLOv12 Test Started")

model = YOLO("yolo12n.pt")

cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()

    if not ret:
        print("Camera error")
        break

    results = model(frame)

    annotated = results[0].plot()

    cv2.imshow("YOLOv12 Detection", annotated)

    if cv2.waitKey(1) == 27:
        break

cap.release()
cv2.destroyAllWindows()