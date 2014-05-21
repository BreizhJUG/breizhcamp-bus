/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel;


import fr.ybonnel.modele.Departure;
import fr.ybonnel.modele.Departures;
import fr.ybonnel.simpleweb4j.exception.HttpErrorException;
import fr.ybonnel.simpleweb4j.handlers.Response;
import fr.ybonnel.simpleweb4j.handlers.Route;
import fr.ybonnel.simpleweb4j.handlers.RouteParameters;
import fr.ybonnel.simpleweb4j.handlers.eventsource.ReactiveStream;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static fr.ybonnel.simpleweb4j.SimpleWeb4j.*;

public class Main {


    private static int getPort() {
        // Cloudbees
        String cloudbeesPort = System.getProperty("app.port");
        if (cloudbeesPort != null) {
            return Integer.parseInt(cloudbeesPort);
        }

        // Default port;
        return 9999;
    }


    public static void main(String[] args) {

        setPort(getPort());
        setPublicResourcesPath("/fr/ybonnel/public");

        jsonp("CALLBACK", new Route<Void, List<Date>>("/next", Void.class) {
            @Override public Response<List<Date>> handle(Void param, RouteParameters routeParams) throws HttpErrorException {
                try {
                    return new Response<List<Date>>(
                            StarApi.getReponseApi("0004", 0, "1160").getOpendata().getAnswer().getData().getStopline()
                                    .getDepartures().getDeparture().stream()
                                    .map(Departure::getContent).collect(Collectors.toList())
                    );
                } catch (IOException e) {
                    return null;
                }
            }
        });

        start();
    }
}
