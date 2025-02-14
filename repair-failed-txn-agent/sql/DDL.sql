CREATE TABLE IF NOT EXISTS repair_failed_txn (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(768) --using the nomic-embed-text embedding model
);

CREATE INDEX ON repair_failed_txn USING HNSW (embedding vector_cosine_ops);