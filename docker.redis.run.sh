#!/bin/sh
docker run --rm -p 6379:6379 --name some-redis -d redis
