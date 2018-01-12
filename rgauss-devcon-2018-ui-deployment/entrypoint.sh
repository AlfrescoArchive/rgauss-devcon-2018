#!/bin/sh

# Bit of a hack to allow local and K8s envs with current app.config
sed -i s%{hostname}%"$ECM_BPM_HOST"%g /usr/share/nginx/html/app.config.json
sed -i s%{:port}%":$ECM_BPM_PORT"%g /usr/share/nginx/html/app.config.json
sed -i '/index  index.html index.htm;/ a\
        try_files $uri $uri/ /index.html;' /etc/nginx/conf.d/default.conf
nginx -g "daemon off;"
