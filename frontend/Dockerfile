# 1. Krok: Použitie oficiálneho Node.js obrazu
FROM node:20-alpine
# Nastavenie pracovného adresára v kontajneri
WORKDIR /app
# Kopírovanie súborov pre závislosti
COPY package*.json ./
# Inštalácia závislostí
RUN npm install
# Kopírovanie zvyšku zdrojového kódu
COPY . .
# Inštalácia Angular CLI globálne (pre príkaz 'ng serve')
RUN npm install -g @angular/cli
# Otvorenie portu 4200
EXPOSE 4200
# Spustenie Angular aplikácie
CMD ["ng", "serve", "--host", "0.0.0.0", "--port", "4200"]
