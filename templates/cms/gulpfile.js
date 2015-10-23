var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    sass = require('gulp-ruby-sass'),
    sourcemaps = require('gulp-sourcemaps');

gulp.task('js', function() {
  return gulp.src('fe/src/main/webapp/wc/cms/js/cms.js')
    .pipe(jshint('./.jshintrc'))
    .pipe(jshint.reporter('jshint-stylish'));
});

gulp.task('sass', function () {
    return sass('fe/src/main/webapp/wc/cms/css/sass/signin.scss', {
      sourcemap: true,
      style: 'expanded'
    })
    .on('error', function (err) {
        console.error('Error!', err.message);
    })
    .pipe(sourcemaps.write())
    .pipe(gulp.dest('fe/src/main/webapp/wc/cms/css'));
});

gulp.task('watch', function() {
  gulp.watch('fe/src/main/webapp/wc/cms/js/**/*', ['js']);
  gulp.watch(['fe/src/main/webapp/wc/cms/css/sass/**/*'], ['sass']);
});
