### This is a makefile for compiling the Java project game-template-example ###


# Dependencies
LIB_GT := game-template
LIB_GT_DIR := ../$(LIB_GT)

include $(LIB_GT_DIR)/common.mk


### Helper functions ###

# Run a command in the $(LIB_GT) directory
run_lib_gt = (cd $(LIB_GT_DIR) && $1)


### Configuration for sources file and compilation artefacts ###

# The name of the jar file to create
JAR_FILE := pascal-mod-n.jar
# The class containing `public static void main(String[] args)`
MAIN_CLASS := pmn.main.PascalMain

# Dependencies
LIB_GT_JAR := $(LIB_GT_DIR)/$(LIB_GT).jar
LIB_GT_SOURCE_DIR := ../$(LIB_GT)/$(SOURCE_DIR)
LIB_GT_CLASS_DIR := ../$(LIB_GT)/$(CLASS_DIR)
LIB_GT_SOURCE_FILES := $(call filter_find,$(LIB_GT_SOURCE_DIR),.java)
### Rules ###

# Define phony targets
.PHONY: all run clean cleanall

### Default rule ###
.DEFAULT_GOAL := all
all: $(JAR_FILE)

### Create game-template.jar ###
$(LIB_GT_JAR) : $(LIB_GT_SOURCE_FILES)
	$(call run_lib_gt,make)

### Compile `.java` files to `.class` files ###
# -cp - Specify classpath
# -d  - Set output directory
# -g  - Generate debugging information
JAVACFLAGS := \
	-cp "$(SOURCE_DIR)$(CP_SEPARATOR)$(LIB_GT_JAR)" \
	-d $(CLASS_DIR) \
	-g:none
# javac [ options ] [ sourcefiles ] [ classes ] [ @argfiles ]
$(CLASS_DIR)/%.class: $(SOURCE_DIR)/%.java | $(LIB_GT_JAR) $(CLASS_DIR)
	$(JAVAC) $(JAVACFLAGS) $<

### Copy `.class` files to the `.jar` archive ###
# c - Create a new archive
# e - Set the application entry point
# f - Specify the file name
# v - Verbose output
JARFLAGS := cef
# jar c[efmMnv0] [entrypoint] [jarfile] [manifest] [-C dir] file ... [-Joption ...] [@arg-file ...]
$(JAR_FILE): $(LIB_GT_JAR) $(CLASS_FILES)
	$(JAR) $(JARFLAGS) $(MAIN_CLASS) $@ -C $(CLASS_DIR) . -C $(LIB_GT_CLASS_DIR) .

### Run the `.jar` archive ###
# java [options] -jar filename [args]
run: $(JAR_FILE)
	$(JAVA) -jar $(JAR_FILE)

### Remove the jar archive and the class files ###
clean:
	$(call run_lib_gt,make clean)
	$(RM) $(JAR_FILE) $(CLASS_FILES)
	
### Remove the jar archive and all class files ###
cleanall:
	$(call run_lib_gt,make cleanall)
	rm -rf $(JAR_FILE) $(CLASS_DIR)
