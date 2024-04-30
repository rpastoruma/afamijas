<?php
/*
 * Created by Artureanec
*/

the_post();
get_header();

$helpo_sidebar_position = helpo_get_prefered_option('sidebar_position');
$helpo_sidebar_name = 'Sidebar';

// --- Page Title Block --- //
if (helpo_get_prefered_option('page_title_status') == 'show') {
    echo helpo_page_title_block_output();
}
?>

<div id="post-<?php the_ID(); ?>" <?php post_class(); ?>>
    <div class="helpo_blog_content_container">
        <div class="helpo_blog_content_wrapper">
            <div class="container">
                <div class="row helpo_sidebar_<?php echo esc_attr($helpo_sidebar_position); ?>">
                    <!-- Content Container -->
                    <div class="col-sm-<?php echo ((is_active_sidebar('sidebar') && $helpo_sidebar_position !== 'none') ? '8' : '12'); ?> col-lg-<?php echo ((is_active_sidebar('sidebar') && $helpo_sidebar_position !== 'none') ? '9' : '12'); ?>">
                        <?php
                        if (helpo_get_prefered_option('media_output_status') == 'show') {
                            echo helpo_post_media_output();
                        }

                        if (helpo_get_prefered_option('post_meta_status') == 'show') {
                            ?>
                            <div class="helpo_post_meta_container">
                                <div class="row">
                                    <div class="col-12 helpo_post_author_container">
                                        <span class="helpo_post_date"><?php echo get_the_date(); ?></span>
                                    </div>
                                </div>
                            </div>
                            <?php
                        }
                        ?>

                        <div class="helpo_content_wrapper">
                            <?php the_content(); ?>
                        </div>

                        <div class="helpo_content_paging_wrapper">
                            <?php wp_link_pages(array('before' => '<div class="page-link">' . esc_html__('Pages', 'helpo') . ': ', 'after' => '</div>')); ?>
                        </div>

                        <?php
                        if (helpo_get_prefered_option('after_content_panel_status') == 'show') {
                            ?>
                            <div class="helpo_post_details_container">
                                <div class="row">
                                    <div class="col-3 helpo_post_details_author_cont">
                                        <span><?php the_author(); ?></span>
                                    </div>

                                    <div class="col-6 helpo_post_details_tag_cont">
                                        <?php the_tags('<ul><li>#', '</li> <li>#', '</li></ul>'); ?>
                                    </div>

                                    <div class="col-3 helpo_post_details_socials_cont">
                                        <?php
                                        if (helpo_get_theme_mod('socials_type') == 'links') {
                                            echo helpo_socials_output('helpo_blog-post__socials');
                                        } else {
                                            ?>
                                            <ul class="helpo_blog-post__socials">
                                                <li>
                                                    <a target="_self"
                                                       href="http://www.facebook.com/share.php?u=<?php echo esc_url(get_permalink()); ?>">
                                                        <i class="fa fa-facebook-f"></i>
                                                    </a>
                                                </li>

                                                <li>
                                                    <a target="_self"
                                                       href="https://twitter.com/intent/tweet?text=<?php echo esc_html(str_replace(' ', '%20', get_the_title())); ?>&amp;url=<?php echo esc_url(get_permalink()); ?>">
                                                        <i class="fa fa-twitter"></i>
                                                    </a>
                                                </li>

                                                <li>
                                                    <a target="_self"
                                                       href="http://www.linkedin.com/shareArticle?mini=true&url=<?php echo esc_url(get_permalink()); ?>&title=<?php echo str_replace(' ', '%20', get_the_title()); ?>&source=htmlweb.ru">
                                                        <i class="fa fa-linkedin"></i>
                                                    </a>
                                                </li>

                                                <li>
                                                    <a target="_self"
                                                       href="http://pinterest.com/pin/create/button/?url=<?php echo esc_url(get_permalink()); ?>&media=<?php echo esc_url((strlen(helpo_get_featured_image_url()) > 0 ? helpo_get_featured_image_url() : helpo_get_theme_mod('logo_image'))); ?>">
                                                        <i class="fa fa-pinterest-p"></i>
                                                    </a>
                                                </li>
                                            </ul>
                                            <?php
                                        }
                                        ?>
                                    </div>
                                </div>
                            </div>
                            <?php
                        }

                        if (helpo_get_prefered_option('comments_status') == 'show') {
                            comments_template();
                        }
                        ?>
                    </div>

                    <!-- Sidebar Container -->
                    <?php get_sidebar(); ?>
                </div>
            </div>
        </div>

        <?php
        if (helpo_get_prefered_option('recent_posts_status') == 'show') {
            helpo_recent_posts_output(
                array(
                    'post_type' => get_post_type(),
                    'orderby' => helpo_get_prefered_option('recent_posts_order_by'),
                    'order' => helpo_get_prefered_option('recent_posts_order'),
                    'numberposts' => helpo_get_prefered_option('recent_posts_number')
                )
            );
        }
        ?>
    </div>
</div>

<?php
get_footer();
