NAME=`gradle printProjectName`

VERSION=`gradle printProjectVersion`

echo '========================================'
echo $NAME
echo $VERSION
echo '========================================'

echo 'run service'

mkdir /home/service
cp ./build/libs/$NAME-$VERSION.jar /home/service/$NAME-$VERSION.jar
cd /home/service
java -jar $NAME-$VERSION.jar
