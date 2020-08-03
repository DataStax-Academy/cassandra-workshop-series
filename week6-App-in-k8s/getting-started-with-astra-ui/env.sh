#!/bin/bash

# Recreate config file
rm -rf ./env-config.js
touch ./env-config.js

# Add assignment 
echo "window._env_ = {" >> ./env-config.js

varname="BASE_ADDRESS"
varvalue=$BASE_ADDRESS

echo "  $varname: \"$varvalue\"," >> ./env-config.js

varname2="USE_ASTRA"
varvalue2=$USE_ASTRA

echo "  $varname2: \"$varvalue2\"," >> ./env-config.js

echo "}" >> ./env-config.js

