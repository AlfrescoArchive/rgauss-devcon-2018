FROM nginx:1.13.6-alpine

COPY entrypoint.sh /
ADD rgauss-devcon-2018-ui-adf-*.tar /usr/share/nginx/html

RUN chmod +x /entrypoint.sh && chown -R root:root /usr/share/nginx/html/*

ENTRYPOINT ["/entrypoint.sh"]