def find_best_match(new_embedding, species_id):

    if new_embedding is None:
        return None, 0

    # 🔥 Normalize again (safety)
    norm = np.linalg.norm(new_embedding)
    if norm == 0:
        return None, 0

    new_embedding = new_embedding / norm

    current_time = time.time()

    # =========================
    # ⚡ MEMORY MATCH
    # =========================
    if species_id in LAST_MATCH:

        prev = LAST_MATCH[species_id]

        score = cosine_similarity(
            new_embedding.reshape(1, -1),
            prev["embedding"].reshape(1, -1)
        )[0][0]

        if score > 0.85 and (current_time - prev["time"] < MEMORY_TIMEOUT):
            print("⚡ Memory match used:", prev["animal_id"])
            return prev["animal_id"], score

    # =========================
    # 🔍 DB MATCH
    # =========================
    stored = load_embeddings(species_id)

    if not stored:
        return None, 0

    best_id = None
    best_score = 0

    for aid, emb in stored.items():

        if emb is None:
            continue

        score = cosine_similarity(
            new_embedding.reshape(1, -1),
            emb.reshape(1, -1)
        )[0][0]

        if score > best_score:
            best_score = score
            best_id = aid

    print(f"🔍 Best Score: {best_score:.3f}")

    # 🔥 Slightly stricter threshold
    if best_score >= SIMILARITY_THRESHOLD:

        LAST_MATCH[species_id] = {
            "animal_id": best_id,
            "embedding": new_embedding,
            "time": current_time
        }

        return best_id, best_score

    return None, best_score