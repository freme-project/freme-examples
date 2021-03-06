/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.eservices.example;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.InternalServerErrorException;
import eu.freme.common.rest.BaseRestController;
import eu.freme.common.rest.NIFParameterSet;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Example e-Service that enriches strings send to it with capitalization.
 * 
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@RestController
public class ECapitalizationService extends BaseRestController {

	Logger logger = Logger.getLogger(ECapitalizationService.class);

	@RequestMapping(value = "/e-capitalization", method = RequestMethod.POST)
	public ResponseEntity<String> example(HttpServletRequest request) {

		NIFParameterSet parameters = getRestHelper().normalizeNif(request, false);
		Model model = getRestHelper().convertInputToRDFModel(parameters);

		Statement textForEnrichment;
		try {
			textForEnrichment = getRdfConversionService().extractFirstPlaintext(model);
		} catch (Exception e) {
			logger.error(e);
			throw new InternalServerErrorException();
		}
		if (textForEnrichment == null) {
			throw new BadRequestException("Could not find input for enrichment");
		}
		String plaintext = textForEnrichment.getObject().asLiteral()
				.getString();
		
		String enrichment = plaintext.toUpperCase();
		Property property = model
				.createProperty("http://freme-project.eu/example-enrichment");
		textForEnrichment.getSubject().addLiteral(property, enrichment);

		return createSuccessResponse(model,
				parameters.getOutformatString());
	}
}
