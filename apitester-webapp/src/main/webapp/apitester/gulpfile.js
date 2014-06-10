var gulp = require('gulp');
var templates = require('gulp-angular-templatecache');
var minifyHTML = require('gulp-minify-html');
var jshint = require('gulp-jshint');
var clean = require('gulp-clean');
var rjs = require('gulp-requirejs');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var tmpl = require('gulp-template');
var rename = require('gulp-rename');
var _ = require('lodash');


// Alias for the default task
gulp.task('package', ['default']);

gulp.task('clean', function(cb){
    gulp.src('build')
        .pipe(clean())
        .on('end', cb)
    ;
});

gulp.task('compilejs', ['clean', 'templates', 'jshint'], function(){
    function almond() {
        var s = gulp.src('build/*.js');
        s.pipe(concat('main.js'))
            .pipe(uglify())
            .pipe(gulp.dest('build/'));
        s.pipe(clean());
    }

    return rjs({
            out: 'require.config.js',
            baseUrl: 'app/src/',
            name: 'require.config',
            mainConfigFile: 'app/src/require.config.js'
        })
        .pipe(gulp.dest('build'))
        .pipe(gulp.src('app/bower_components/almond/almond.js'))
        .pipe(gulp.dest('build'))
        .on('end', almond)
    ;
});

gulp.task('templates', function() {
    return gulp.src(['app/src/**/*.html'])
        .pipe(minifyHTML({quotes: true, empty:true}))
        .pipe(templates({standalone: true}))
        .pipe(gulp.dest('app'))
});

gulp.task('jshint', function(){
    var stream = gulp.src(['app/src/**/*.js'])
        .pipe(jshint())
        .pipe(jshint.reporter(require('jshint-stylish')))
    ;
    if (!isWatch) {
        stream.pipe(jshint.reporter('fail'))
    }
    return stream;
});

var isWatch = false;
gulp.task('watch', ['templates'],function() {
    isWatch = true;
    gulp.watch('app/src/**/*.html', ['templates']);
    gulp.watch(['app/src/**/*.js', 'tests/**/*.js'], ['jshint']);
});