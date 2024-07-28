#!/bin/bash
set -e

function build() {
  npm run build -- --base-href '/webapp/'
}

function sync_to_backend() {
  backend_output_location=../api/src/main/resources/static/webapp

  echo "[Sync files to backend in progress...] Output location ${backend_output_location}"

  if [[ -d ${backend_output_location} ]]; then
    rm -r ${backend_output_location}
  fi

  mkdir -p ${backend_output_location}
  cp -R ./dist/web/browser/* ${backend_output_location}

  echo "[Sync files to backend succeeded] Output location ${backend_output_location}"
}

build
sync_to_backend
