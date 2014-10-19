git fetch
git reset --hard origin/master
mvn package -DskipTests=true
cp target/TR*.jar ../BotDir
cp target/lib/* ../BotDir/lib

echo "Last build: " > last_build
date >> last_build

echo "Build complete"