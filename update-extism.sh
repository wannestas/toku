#!/bin/sh
set -e

EXTISM_VERSION=$(curl https://api.github.com/repos/extism/extism/releases/latest | jq -r '.name')

echo "latest extism version is: ${EXTISM_VERSION}"

mkdir -p ./src/commonMain/resources/natives/

create_librairies_folders() {
  archs=("darwin-aarch64" "darwin-x86-64" "linux-aarch64" "linux-x86-64" "win32-x86-64")
  for i in ${archs[@]}; do
    rm -rf ./src/commonMain/resources/$i;
    mkdir ./src/commonMain/resources/$i
  done
}

fetch_and_unzip_library() {
  ARCH="$1"
  LIBRARY_FOLDER="$2"
  FILENAME="$3"

  curl -L -o "./src/commonMain/resources/natives/${ARCH}-${EXTISM_VERSION}.tar.gz" "https://github.com/extism/extism/releases/download/${EXTISM_VERSION}/${ARCH}-${EXTISM_VERSION}.tar.gz"
  tar -xvf "./src/commonMain/resources/natives/${ARCH}-${EXTISM_VERSION}.tar.gz" --directory ./src/commonMain/resources/natives/
  mv ./src/commonMain/resources/natives/${FILENAME} ./src/commonMain/resources/${LIBRARY_FOLDER}/${FILENAME}
}

create_librairies_folders

fetch_and_unzip_library "libextism-aarch64-apple-darwin" "darwin-aarch64" "libextism.dylib"
fetch_and_unzip_library "libextism-x86_64-apple-darwin" "darwin-x86-64" "libextism.dylib"
fetch_and_unzip_library "libextism-aarch64-unknown-linux-gnu" "linux-aarch64" "libextism.so"
fetch_and_unzip_library "libextism-x86_64-unknown-linux-gnu" "linux-x86-64" "libextism.so"
fetch_and_unzip_library "libextism-x86_64-pc-windows-gnu" "win32-x86-64" "extism.dll"

rm -rf ./src/commonMain/resources/natives