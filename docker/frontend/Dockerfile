# Build context is the repository root so we can reach both the frontend
# submodule and this docker/ directory.
FROM node:20 AS build
WORKDIR /app

# Skip Chromium download from the puppeteer devDependency – it is only used for tests
ENV PUPPETEER_SKIP_DOWNLOAD=true

COPY frontend/package*.json ./
# The starter pins Angular 17 while some transitive deps still declare peer
# ranges for Angular 16, so relax peer-dependency resolution during install.
RUN npm install --legacy-peer-deps

COPY frontend/ ./
RUN npm run build

FROM nginx:1.27-alpine
COPY docker/frontend/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/template-frontend /usr/share/nginx/html
EXPOSE 80
