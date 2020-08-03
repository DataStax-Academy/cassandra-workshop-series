## React JS UI

The UI application has been updated to be able start and serve successfully in a container, configurable by an environment variable for the BASE_ADDRESS

- TODO: what happens if no environment variable is set? 

- If you want to connect to local instance the following properties are reqired


## Build the docker image

```
docker build -t astra-ui:0802-image .
```

Note that when building the image, a tag needs to be provided so that we can use it in a kind kubernetes cluster.

## Run the docker image

```
docker run -e BASE_ADDRESS='http://localhost:5000/api' -p 3000:3000 astra-ui:0802-image 
```

This will  configure the UI to work with BASE_ADDRESS http://localhost:5000/api and will expose and publish the port 3000 to localhost:3000 on the docker host. 

📗 **Expected output when running the docker image**
```
$ docker run -e BASE_ADDRESS='http://localhost:5000/api' -p 3000:3000 astra-ui:0802-image 

> getting-started-with-apollo-ui@1.0.0 start /app
> chmod +x ./env.sh && ./env.sh && cross-env NODE_ENV=development webpack-dev-server -d --host=0.0.0.0

ℹ ｢wds｣: Project is running at http://0.0.0.0:3000/
ℹ ｢wds｣: webpack output is served from /
ℹ ｢wds｣: Content not from webpack is served from /app
ℹ ｢wds｣: 404s will fallback to /index.html
Browserslist: caniuse-lite is outdated. Please run next command `npm update`
ℹ ｢wdm｣: Hash: b241973c1523df8231af
Version: webpack 4.41.1
Time: 8106ms
Built at: 08/02/2020 12:30:36 PM
                               Asset       Size  Chunks             Chunk Names
133555249693bd2fcfde30e82c78a51f.otf    139 KiB          [emitted]  
1bb750038b4c847c450de068e5e35483.svg   12.3 KiB          [emitted]  
426f079e09cef7f2af6a2ca17515eeb9.otf    135 KiB          [emitted]  
61dfd3ba939231c2d4103c84d8aa3718.otf    134 KiB          [emitted]  
                          index.html  393 bytes          [emitted]  
                            index.js   18.2 MiB    main  [emitted]  main
Entrypoint main = index.js
[0] multi (webpack)-dev-server/client?http://0.0.0.0:3000 (webpack)/hot/dev-server.js @babel/polyfill whatwg-fetch react-hot-loader/patch ./src/index.jsx 88 bytes {main} [built]
[./node_modules/@babel/polyfill/lib/index.js] 686 bytes {main} [built]
[./node_modules/@babel/polyfill/lib/noConflict.js] 567 bytes {main} [built]
[./node_modules/core-js/library/fn/global.js] 87 bytes {main} [built]
[./node_modules/react-dom/index.js] 1.33 KiB {main} [built]
[./node_modules/react-hot-loader/dist/react-hot-loader.development.js] 97.1 KiB {main} [built]
[./node_modules/react-hot-loader/index.js] 1.24 KiB {main} [built]
[./node_modules/react-hot-loader/patch.js] 229 bytes {main} [built]
[./node_modules/react/index.js] 190 bytes {main} [built]
[./node_modules/webpack-dev-server/client/index.js?http://0.0.0.0:3000] (webpack)-dev-server/client?http://0.0.0.0:3000 4.29 KiB {main} [built]
[./node_modules/webpack-dev-server/client/overlay.js] (webpack)-dev-server/client/overlay.js 3.51 KiB {main} [built]
[./node_modules/webpack-dev-server/client/socket.js] (webpack)-dev-server/client/socket.js 1.53 KiB {main} [built]
[./node_modules/webpack/hot/dev-server.js] (webpack)/hot/dev-server.js 1.59 KiB {main} [built]
[./node_modules/whatwg-fetch/fetch.js] 13 KiB {main} [built]
[./src/index.jsx] 1.48 KiB {main} [built]
    + 2103 hidden modules
Child html-webpack-plugin for "index.html":
     1 asset
    Entrypoint undefined = index.html
    [./node_modules/html-webpack-plugin/lib/loader.js!./src/index.html] 543 bytes {0} [built]
    [./node_modules/lodash/lodash.js] 528 KiB {0} [built]
    [./node_modules/webpack/buildin/global.js] (webpack)/buildin/global.js 472 bytes {0} [built]
    [./node_modules/webpack/buildin/module.js] (webpack)/buildin/module.js 497 bytes {0} [built]
ℹ ｢wdm｣: Compiled successfully.
```


**Using the docker image in kind**

You need to have a running kind cluster. We assume the name of the cluster is `kind-cassandra`.

The docker image needs to loaded into the kind cluster:

```
kind load docker-image astra-ui:0802-image --name kind-cassandra
```

📗 **Expected output**

to be continued. 
















