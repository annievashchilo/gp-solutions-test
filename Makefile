.PHONY: all format lint clean test

WORKING_DIR ?= ./propertyview/

all:
	cd $(WORKING_DIR) && mvn clean spotless:apply test

format:
	cd $(WORKING_DIR) && mvn spotless:apply

lint:
	cd $(WORKING_DIR) && mvn checkstyle:check

clean:
	cd $(WORKING_DIR) && mvn clean

test:
	cd $(WORKING_DIR) && mvn test