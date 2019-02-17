NAME=`gradle -q printProjectName`

VERSION=`gradle -q printProjectVersion`

echo '========================================'
echo $NAME
echo $VERSION
echo '========================================'


mkdir /home/service
cp ./build/libs/$NAME-$VERSION.jar /home/service/$NAME-$VERSION.jar

