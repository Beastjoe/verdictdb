git remote add doc-origin git@github.com:verdictdb/verdictdb-doc.git
git checkout master
cd docs
mkdocs build
cd ..
git subtree split --prefix docs/site -b gh-pages
git push -f doc-origin gh-pages:gh-pages
git branch -D gh-pages