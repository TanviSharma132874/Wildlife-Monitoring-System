import torch
import torchvision.models as models
import torchvision.transforms as transforms
import numpy as np
from PIL import Image

# =========================
# LOAD MODEL (ONCE)
# =========================
device = torch.device("cpu")

resnet = models.resnet50(weights=models.ResNet50_Weights.DEFAULT)
resnet = torch.nn.Sequential(*list(resnet.children())[:-1])
resnet.eval()
resnet.to(device)

# =========================
# TRANSFORM
# =========================
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(
        mean=[0.485, 0.456, 0.406],
        std=[0.229, 0.224, 0.225]
    )
])

# =========================
# GENERATE EMBEDDING
# =========================
def generate_embedding(image_path):
    try:
        img = Image.open(image_path).convert("RGB")
    except Exception as e:
        print("❌ Image load failed:", image_path)
        return None

    img = transform(img).unsqueeze(0).to(device)

    with torch.no_grad():
        features = resnet(img)

    emb = features.squeeze().cpu().numpy().astype(np.float32)

    # 🔥 CRITICAL FIX: handle bad embeddings
    norm = np.linalg.norm(emb)

    if norm == 0 or np.isnan(norm):
        print("❌ Invalid embedding generated")
        return None

    emb = emb / norm

    # 🔍 Debug (optional)
    print("Embedding norm:", np.linalg.norm(emb))
    print("Embedding sample:", emb[:5])

    return emb