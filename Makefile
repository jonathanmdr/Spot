.PHONY: clean install test up down restart

clean:
	@gradle clean

install:
	@gradle clean build

test:
	@gradle clean test

up:
	@docker-compose up -d

down:
	@docker-compose down --remove-orphans --volumes

restart:
	@docker-compose restart
