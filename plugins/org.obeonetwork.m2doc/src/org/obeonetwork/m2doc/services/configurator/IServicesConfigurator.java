/*******************************************************************************
 *  Copyright (c) 2017 Obeo. 
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *   
 *   Contributors:
 *       Obeo - initial API and implementation
 *  
 *******************************************************************************/
package org.obeonetwork.m2doc.services.configurator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.acceleo.query.runtime.IReadOnlyQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;

/**
 * Configure for {@link IReadOnlyQueryEnvironment}.
 * 
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface IServicesConfigurator {

    /**
     * Gets the {@link List} of options managed by this configurator.
     * 
     * @return the {@link List} of options managed by this configurator
     */
    List<String> getOptions();

    /**
     * Gets the {@link Map} of initialized options.
     * 
     * @param options
     *            the {@link Map} of existing options.
     * @return the {@link Map} of initialized options
     */
    Map<String, String> getInitializedOptions(Map<String, String> options);

    /**
     * Validates the given options.
     * 
     * @param queryEnvironment
     *            the {@link IReadOnlyQueryEnvironment}
     * @param options
     *            the {@link Map} of options
     * @return the {@link Map} of option name to its {@link Diagnostic}
     */
    Map<String, List<Diagnostic>> validate(IReadOnlyQueryEnvironment queryEnvironment, Map<String, String> options);

    /**
     * Gets the {@link Set} of {@link IService} for the given {@link IReadOnlyQueryEnvironment} and options.
     * 
     * @param queryEnvironment
     *            the {@link IReadOnlyQueryEnvironment}
     * @param uriConverter
     *            the {@link URIConverter}
     * @param options
     *            the {@link Map} of options
     * @return the {@link Set} of {@link IService} for the given {@link IReadOnlyQueryEnvironment}
     */
    Set<IService> getServices(IReadOnlyQueryEnvironment queryEnvironment, URIConverter uriConverter,
            Map<String, String> options);

    /**
     * Clears the services for the given {@link IReadOnlyQueryEnvironment}.
     * 
     * @param queryEnvironment
     *            the {@link IReadOnlyQueryEnvironment}
     * @param uriConverter
     *            the {@link URIConverter}
     */
    void cleanServices(IReadOnlyQueryEnvironment queryEnvironment, URIConverter uriConverter);

    /**
     * Create a new resourceSet which would need specific initialization for loading the models according to the given options.
     * 
     * @param queryEnvironment
     *            the {@link IReadOnlyQueryEnvironment}
     * @param options
     *            the {@link Map} of options
     * @return the created {@link ResourceSet} if any, <code>null</code> otherwise
     * @see #cleanResourceSetForModels(IReadOnlyQueryEnvironment)
     */
    ResourceSet createResourceSetForModels(IReadOnlyQueryEnvironment queryEnvironment, Map<String, String> options);

    /**
     * Cleans the {@link #createResourceSetForModels(IReadOnlyQueryEnvironment, Map) created} {@link ResourceSet} for the given
     * {@link IReadOnlyQueryEnvironment}.
     * 
     * @param queryEnvironment
     *            the {@link IReadOnlyQueryEnvironment}
     */
    void cleanResourceSetForModels(IReadOnlyQueryEnvironment queryEnvironment);

}
