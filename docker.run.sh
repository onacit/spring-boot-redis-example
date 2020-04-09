#!/bin/sh
docker run --rm -p 36379:6379 --name some-redis -d redis
