
# Build Docker Image

You must first build the tar artifact.  See [rgauss-devcon-2018-ui](../rgauss-devcon-2018-ui).

To build the Docker image run the script which gets the app distribution artifact:

```bash
./build-prep.sh
```

if building to deploy within Minikube:

```bash
eval $(minikube docker-env)
```

build the service Docker image:

```bash
docker build -t alfresco/rgauss-devcon-2018-ui:0.1-SNAPSHOT .
```

# Helm

[Get your ELB address](https://github.com/Alfresco/alfresco-dbp-deployment#6-get-minikube-or-elb-ip-and-set-it-as-a-variable-for-future-use), i.e.:

```bash
export ELBADDRESS=$(minikube ip)
```

Run the helm commands:

```bash
cd helm
helm install rgauss-devcon-2018-ui \
--set ecmbpm.host="$ELBADDRESS"
```

You can then open the UI in your browser:

```bash
open http://$ELBADDRESS/rgauss-devcon-2018-ui/
```

Note the trailing slash.
