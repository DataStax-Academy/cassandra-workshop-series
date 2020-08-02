#!/bin/bash

# Recreate config file
rm -rf ./env-config.js
touch ./env-config.js

# Add assignment 
echo "window._env_ = {" >> ./env-config.js

varname="BASE_ADDRESS"
varvalue=$BASE_ADDRESS

echo "  $varname: \"$varvalue\"," >> ./env-config.js

echo "}" >> ./env-config.js

