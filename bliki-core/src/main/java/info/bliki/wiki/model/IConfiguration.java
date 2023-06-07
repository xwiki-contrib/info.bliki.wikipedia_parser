package info.bliki.wiki.model;

import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.template.ITemplateFunction;

import java.util.Map;
import java.util.Set;

/**
 * Configuration interface for extensions similar to the Mediawiki
 * <a href="https://www.mediawiki.org/wiki/Manual:Tag_extensions">HTML styled tag extension</a>,
 * <a href="https://www.mediawiki.org/wiki/Manual:Parser_functions">template parser functions</a>
 * and <a href="https://www.mediawiki.org/wiki/Interwiki">interwiki links</a>
 *
 * @see info.bliki.wiki.model.Configuration
 */
public interface IConfiguration {
    /**
     * @see <a href="https://meta.wikimedia.org/wiki/Help:Page_name#Case-sensitivity">Case-sensitivity</a>
     */
    enum Casing {
        CaseSensitive,
        FirstLetter
    }

    /**
     * Add a new
     * <a href="https://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi">source code formatter</a>
     * to the configuration
     */
    SourceCodeFormatter addCodeFormatter(String key, SourceCodeFormatter value);

    /**
     * Add another Interwiki link to the configuration. The value string must
     * contain the <code>$1</code> placeholder for the used wiki article
     * link.
     *
     * @param key   the prefix used in the interwiki link (i.e. [[prefix:...]] )
     * @param value the external link which should be generated from the interwiki
     *              link
     */
    String addInterwikiLink(String key, String value);

    /**
     * Add a new <a href="https://www.mediawiki.org/wiki/Interwiki">interwiki
     * link</a> to the configuration
     */
    ITemplateFunction addTemplateFunction(String key, ITemplateFunction value);

    /**
     * Add a new
     * <a href="https://www.mediawiki.org/wiki/Manual:Tag_extensions">HTML styled tag</a> to the configuration
     */
    TagToken addTokenTag(String key, TagToken value);

    /**
     * Get the <a href="https://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi">source code formatter</a> map of built-in source code formatters
     */
    Map<String, SourceCodeFormatter> getCodeFormatterMap();

    /**
     * Get the currently configured cache implementation for template calls.
     *
     * @return <code>null</code> if no cache implementation is set.
     * @see IConfiguration#setTemplateCallsCache(Map)
     */
    Map<String, String> getTemplateCallsCache();

    /**
     * Get the <a
     * href="https://www.mediawiki.org/wiki/Manual:Parser_functions">template
     * parser functions</a> map of built-in template functions
     */
    Map<String, ITemplateFunction> getTemplateMap();

    /**
     * Get the <a href="https://www.mediawiki.org/wiki/Manual:Tag_extensions">HTML
     * styled tag</a> map for built-in tags
     */
    Map<String, TagToken> getTokenMap();

    /**
     * Get the set of all allowed URI scheme shortcuts like http, https, ftp,...
     *
     * See <a href="https://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
     */
    Set<String> getUriSchemeSet();

    /**
     * Set a cache map implementation. For example based on <a
     * href="https://jcp.org/en/jsr/detail?id=107">JSR 107</a>. Template calls
     * which use the same parameters over and over again do lookup this cache and
     * use the preparsed result if available.
     *
     * <b>Note:</b> don't use a simple java.util.HashMap implementation because it's not thread-safe an grows infinitly.
     *
     * @param map
     *          the cache implementation
     */
    void setTemplateCallsCache(Map<String, String> map);

    /**
     * @return the default casing, defaults to {@link Casing#FirstLetter}.
     */
    Casing casing();

    /**
     * @return indicate if the caption should always be forced below the image
     */
    boolean isForceFramedImage();

    /**
     * @param forceFramedImage indicate if the caption should always be forced below the image
     */
    void setForceFramedImage(boolean forceFramedImage);
}
