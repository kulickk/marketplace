# Используем официальный Node.js образ
FROM node:20

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем package.json и package-lock.json
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем весь проект
COPY . .

# Указываем порт, который используется приложением (по умолчанию 3000)
EXPOSE 3000

# Запускаем в режиме разработки
CMD ["npm", "run", "dev"]