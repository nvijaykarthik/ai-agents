CREATE TABLE IF NOT EXISTS email_alert (
	id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
	content text,
	metadata json,
	embedding vector(768) --using the nomic-embed-text embedding model
);

CREATE INDEX ON email_alert USING HNSW (embedding vector_cosine_ops);