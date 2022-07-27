#! /bin/sh
cp /Users/manwelbugeja/git/android-attacks/DynamicCode/app/build/intermediates/apk/debug/app-debug.apk .
git add debug-app.apk
git commit -m "Updated apk"
git push
