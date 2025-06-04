import os
import uuid
import random
import decimal
from datetime import datetime

import psycopg2
from psycopg2.extras import execute_values
from faker import Faker
from PIL import Image, ImageDraw, ImageFont

# pip install psycopg2-binary faker pillow

DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "marketplace"
DB_USER = "postgres"
DB_PASSWORD = "pass"

UPLOAD_DIR = "uploads/goods"

DEFAULT_CATEGORY_ID = "6f0b4640-7ffd-4220-8cc1-fd7229573029"


os.makedirs(UPLOAD_DIR, exist_ok=True)


def generate_placeholder_image(text: str, save_path: str):
    width, height = 400, 400

    bg_color = tuple(random.randint(100, 255) for _ in range(3))
    img = Image.new("RGB", (width, height), bg_color)
    draw = ImageDraw.Draw(img)

    font = ImageFont.load_default()

    if len(text) > 20:
        text = text[:17] + "..."

    bbox = draw.textbbox((0, 0), text, font=font)
    text_w = bbox[2] - bbox[0]
    text_h = bbox[3] - bbox[1]

    x = (width - text_w) // 2
    y = (height - text_h) // 2

    brightness = (bg_color[0] * 299 + bg_color[1] * 587 + bg_color[2] * 114) / 1000
    text_color = (0, 0, 0) if brightness > 150 else (255, 255, 255)

    draw.text((x, y), text, fill=text_color, font=font)
    img.save(save_path, format="PNG")


def main():
    faker = Faker("ru_RU")

    conn = psycopg2.connect(
        host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD
    )
    cursor = conn.cursor()

    insert_goods_sql = """
        INSERT INTO goods (id, name, description, price, stock, brand, category_id)
        VALUES %s
        RETURNING id
    """
    insert_image_sql = """
        INSERT INTO good_images (id, good_id, image_path)
        VALUES %s
    """

    goods_values = []
    image_values = []

    for _ in range(100):
        good_id = str(uuid.uuid4())

        name = faker.word().capitalize() + " " + faker.word().capitalize()
        description = faker.sentence(nb_words=8, variable_nb_words=True)
        price = decimal.Decimal(f"{random.uniform(100, 50000):.2f}")
        stock = random.randint(1, 500)
        brand = faker.company()

        category_id = DEFAULT_CATEGORY_ID

        goods_values.append(
            (good_id, name, description, price, stock, brand, category_id)
        )

        image_filename = f"{good_id}.png"
        image_path = os.path.join(UPLOAD_DIR, image_filename)
        generate_placeholder_image(name, image_path)

        image_id = str(uuid.uuid4())
        image_values.append((image_id, good_id, image_filename))

    execute_values(cursor, insert_goods_sql, goods_values)
    print(f"Вставлено {len(goods_values)} записей в таблицу goods.")

    execute_values(cursor, insert_image_sql, image_values)
    print(f"Вставлено {len(image_values)} записей в таблицу good_images.")

    conn.commit()
    cursor.close()
    conn.close()
    print("Генерация завершена. Изображения лежат в папке:", UPLOAD_DIR)


if __name__ == "__main__":
    main()
