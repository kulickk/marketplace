CREATE TABLE good_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    good_id UUID NOT NULL,
    image_path VARCHAR(255) NOT NULL
);

ALTER TABLE good_images
  ADD CONSTRAINT fk_good_images_good
  FOREIGN KEY (good_id)
  REFERENCES goods (id)
  ON DELETE CASCADE;