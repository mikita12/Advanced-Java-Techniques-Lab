#!/usr/bin/env bash
# run-image.sh - uruchamia jlink-generated runtime image
# Zakładamy, że obraz został wygenerowany w target/image
IMAGE_DIR="target/image"
if [ ! -d "$IMAGE_DIR" ]; then
  echo "Image not found in $IMAGE_DIR. Build it with: mvn -DskipTests javafx:jlink"
  exit 1
fi

# Execute module: module-name/main-class
# module: api_quiz, main class: ui.MainApp (module:mainClass format)
BIN="$IMAGE_DIR/bin/java"
if [ ! -x "$BIN" ]; then
  echo "Java launcher not found or not executable: $BIN"
  exit 2
fi

"$BIN" -m api_quiz/ui.MainApp "$@"
