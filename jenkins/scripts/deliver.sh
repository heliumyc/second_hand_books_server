set -x
NAME = `gradle printProjectName`
set +x

set -x
VERSION = `gradle printProjectVersion`
set +x

echo NAME