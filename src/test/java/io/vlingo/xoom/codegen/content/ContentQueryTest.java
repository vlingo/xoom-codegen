// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.content;

import io.vlingo.xoom.codegen.template.OutputFile;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static io.vlingo.xoom.codegen.content.FakeTemplateStandards.*;

public class ContentQueryTest {

    private static final String AUTHOR_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
            "import io.vlingo.xoom.symbio.store.object.StateObject; \\n" +
            "public class AuthorState { \\n" +
            "... \\n" +
            "}";

    private static final String BOOK_STATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
                    "import io.vlingo.xoom.symbio.store.object.StateObject; \\n" +
                    "public class BookState { \\n" +
                    "... \\n" +
                    "}";

    private static final String AGGREGATE_CONTENT_TEXT =
            "package io.vlingo.xoomapp.model; \\n" +
            "public class Author { \\n" +
            "... \\n" +
             "}";
    private static final String AGGREGATE_PROTOCOL_CONTENT_TEXT =
        "package io.vlingo.xoomapp.model; \\n" +
            "public interface Author { \\n" +
            "... \\n" +
            "}";

    private static final String AUTHOR_CONTENT_TEXT =
        "using Vlingo.Xoom.Actors;\\n" +
            "using Vlingo.Xoom.Common;\\n" +
            "namespace Io.Vlingo.Xoomapp.Model; \\n" +
            "public interface IAuthor { \\n" +
            "... \\n" +
            "}";
    private static final String AUTHOR_CONTENT_TEXT_WITHOUT_IMPORTS =
        "namespace Io.Vlingo.Xoomapp.Model; \\n" +
            "public interface IAuthor { \\n" +
            "... \\n" +
            "}";

    @Test
    public void testClassNameQuery() {
        final Set<String> classNames =
                ContentQuery.findClassNames(AGGREGATE_STATE, contents());

        Assert.assertEquals(2, classNames.size());
        Assert.assertTrue(classNames.contains("AuthorState"));
        Assert.assertTrue(classNames.contains("BookState"));
    }

    @Test
    public void testQualifiedClassNameQuery() {
        final Set<String> classNames =
                ContentQuery.findFullyQualifiedClassNames(AGGREGATE_STATE, contents());

        Assert.assertEquals(2, classNames.size());
        Assert.assertTrue(classNames.contains("io.vlingo.xoomapp.model.AuthorState"));
        Assert.assertTrue(classNames.contains("io.vlingo.xoomapp.model.BookState"));
    }

    @Test
    public void testFindPackageForJava() {
        final String packageName = ContentQuery.findPackage(AGGREGATE_PROTOCOL, "Author", contents());

        Assert.assertEquals("io.vlingo.xoomapp.model", packageName);
    }

    @Test
    public void testFindPackageForCsharp() {
        final List<Content> contents = Collections.singletonList(Content.with(AGGREGATE_PROTOCOL,
                new OutputFile("/Projects/", "IAuthor.cs"),
                null, null, AUTHOR_CONTENT_TEXT));

        final String packageName = ContentQuery.findPackage(AGGREGATE_PROTOCOL, "IAuthor", contents);

        Assert.assertEquals("Io.Vlingo.Xoomapp.Model", packageName);
    }

    @Test
    public void testFindPackageForCsharpWithoutImports() {
        final List<Content> contents = Collections.singletonList(Content.with(AGGREGATE_PROTOCOL,
            new OutputFile("/Projects/", "IAuthor.cs"),
            null, null, AUTHOR_CONTENT_TEXT_WITHOUT_IMPORTS));

        final String packageName = ContentQuery.findPackage(AGGREGATE_PROTOCOL, "IAuthor", contents);

        Assert.assertEquals("Io.Vlingo.Xoomapp.Model", packageName);
    }

    private List<Content> contents() {
        return Arrays.asList(
            Content.with(AGGREGATE_STATE, new OutputFile("/Projects/", "AuthorState.java"), null, null, AUTHOR_STATE_CONTENT_TEXT),
            Content.with(AGGREGATE_STATE, new OutputFile("/Projects/", "BookState.java"), null, null, BOOK_STATE_CONTENT_TEXT),
            Content.with(AGGREGATE, new OutputFile("/Projects/", "Author.java"), null, null, AGGREGATE_CONTENT_TEXT),
            Content.with(AGGREGATE_PROTOCOL, new OutputFile("/Projects/", "Author.java"), null, null, AGGREGATE_PROTOCOL_CONTENT_TEXT)
        );
    }
}
