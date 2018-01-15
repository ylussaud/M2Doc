/*******************************************************************************
 *  Copyright (c) 2018 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.m2doc.html.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.eclipse.acceleo.annotations.api.documentation.ServiceProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

//@formatter:off
@ServiceProvider(
value = "Services available for HTML insertion."
)
//@formatter:on
@SuppressWarnings({"checkstyle:javadocmethod", "checkstyle:javadoctype"})
public class M2DocHTMLServices {

    // In itialize log3j.
    {
        BasicConfigurator.configure();
    }

    /**
     * The URI converter to use.
     */
    private final URIConverter uriConverter;

    /**
     * The template URI.
     */
    private final URI templateURI;

    /**
     * Constructor.
     * 
     * @param uriConverter
     *            the {@link URIConverter uri converter} to use.
     * @param templateURI
     *            the template {@link URI}
     */
    public M2DocHTMLServices(URIConverter uriConverter, URI templateURI) {
        this.uriConverter = uriConverter;
        this.templateURI = templateURI;
    }

    public XWPFDocument fromHTMLString(String htmlString) throws IOException, JAXBException, Docx4JException {
        return toXWPFDocument(templateURI, htmlString);
    }

    public XWPFDocument fromHTMLString(String htmlString, String baseURI)
            throws IOException, JAXBException, Docx4JException {
        return toXWPFDocument(URI.createURI(baseURI, false), htmlString);
    }

    public XWPFDocument fromHTMLURI(String uriStr) throws IOException, JAXBException, Docx4JException {
        final URI htmlURI = URI.createURI(uriStr, false);
        final URI uri = htmlURI.resolve(templateURI);

        try (InputStream input = uriConverter.createInputStream(uri);) {
            final String htmlString = IOUtils.toString(input, "UTF-8");

            return toXWPFDocument(uri, htmlString);
        }
    }

    private XWPFDocument toXWPFDocument(URI baseURI, String html)
            throws InvalidFormatException, JAXBException, Docx4JException, IOException {
        final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        final NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
        ndp.unmarshalDefaultNumbering();
        final XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

        xHTMLImporter.setHyperlinkStyle("Hyperlink");
        final String xhtmlString = toXHTML(baseURI.toString(), html);
        wordMLPackage.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(xhtmlString, baseURI.toString()));
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        wordMLPackage.save(output);
        final ByteArrayInputStream xhtmlDocx = new ByteArrayInputStream(output.toByteArray());

        return new XWPFDocument(xhtmlDocx);
    }

    /**
     * Converts the given html {@link String} to a valid XHTML {@link String}.
     * 
     * @param baseURI
     *            the base URI
     * @param htmlString
     *            the HTML {@link String}
     * @return the converted XHTML {@link String}
     */
    private String toXHTML(String baseURI, String htmlString) {
        final Document document = Jsoup.parse(htmlString, baseURI);

        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        document.outputSettings().charset("UTF-8");

        return Parser.unescapeEntities(document.html(), true);
    }

}
