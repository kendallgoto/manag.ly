FROM node:15-alpine
RUN apk add --no-cache git
RUN npm install pm2 -g

WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .

ADD https://github.com/Yelp/dumb-init/releases/download/v1.2.2/dumb-init_1.2.2_x86_64 /usr/local/bin/dumb-init
RUN chmod +x /usr/local/bin/dumb-init
ENTRYPOINT ["dumb-init", "--"]

CMD ["pm2-runtime", "ecosystem.config.js"]
