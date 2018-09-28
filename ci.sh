#!/bin/bash
cd $(dirname $0)/00 - Scaffold/

set -e

sudo rm -rf build
mvn clean
sudo rm -rf target