FILE=paper-1.20.2-260.jar
SLEEP_TIME=3
MIN_MEMORY=512M
MAX_MEMORY=2048M

echo "Starting $FILE..."

while true; do
  java -Xms$MIN_MEMORY -Xmx$MAX_MEMORY -jar $FILE nogui
  echo "Use <CTRL+C> to leave."
  sleep $SLEEP_TIME
  echo "Restart..."
done
