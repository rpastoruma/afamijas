<?php

if (!function_exists('art_css_js_child')) {
add_action('wp_enqueue_scripts', 'art_css_js_child');
    function art_css_js_child() {
        $parent_style = 'parent-style';
        wp_enqueue_style( $parent_style, get_template_directory_uri() . '/style.css' );

    	wp_enqueue_style( 'child-style',
        get_stylesheet_directory_uri() . '/style.css',
        array( $parent_style )
    );
    }
}

/**
 * Your code here.
 *
 */