package org.jhipster.health.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.health.domain.Preferences;

import org.jhipster.health.repository.PreferencesRepository;
import org.jhipster.health.repository.search.PreferencesSearchRepository;
import org.jhipster.health.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Preferences.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);
        
    @Inject
    private PreferencesRepository preferencesRepository;

    @Inject
    private PreferencesSearchRepository preferencesSearchRepository;

    /**
     * POST  /preferences : Create a new preferences.
     *
     * @param preferences the preferences to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preferences, or with status 400 (Bad Request) if the preferences has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/preferences")
    @Timed
    public ResponseEntity<Preferences> createPreferences(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferences);
        if (preferences.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("preferences", "idexists", "A new preferences cannot already have an ID")).body(null);
        }
        Preferences result = preferencesRepository.save(preferences);
        preferencesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("preferences", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferences : Updates an existing preferences.
     *
     * @param preferences the preferences to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preferences,
     * or with status 400 (Bad Request) if the preferences is not valid,
     * or with status 500 (Internal Server Error) if the preferences couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/preferences")
    @Timed
    public ResponseEntity<Preferences> updatePreferences(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferences);
        if (preferences.getId() == null) {
            return createPreferences(preferences);
        }
        Preferences result = preferencesRepository.save(preferences);
        preferencesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("preferences", preferences.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferences : get all the preferences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of preferences in body
     */
    @GetMapping("/preferences")
    @Timed
    public List<Preferences> getAllPreferences() {
        log.debug("REST request to get all Preferences");
        List<Preferences> preferences = preferencesRepository.findAll();
        return preferences;
    }

    /**
     * GET  /preferences/:id : get the "id" preferences.
     *
     * @param id the id of the preferences to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preferences, or with status 404 (Not Found)
     */
    @GetMapping("/preferences/{id}")
    @Timed
    public ResponseEntity<Preferences> getPreferences(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        Preferences preferences = preferencesRepository.findOne(id);
        return Optional.ofNullable(preferences)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /preferences/:id : delete the "id" preferences.
     *
     * @param id the id of the preferences to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/preferences/{id}")
    @Timed
    public ResponseEntity<Void> deletePreferences(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);
        preferencesRepository.delete(id);
        preferencesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("preferences", id.toString())).build();
    }

    /**
     * SEARCH  /_search/preferences?query=:query : search for the preferences corresponding
     * to the query.
     *
     * @param query the query of the preferences search 
     * @return the result of the search
     */
    @GetMapping("/_search/preferences")
    @Timed
    public List<Preferences> searchPreferences(@RequestParam String query) {
        log.debug("REST request to search Preferences for query {}", query);
        return StreamSupport
            .stream(preferencesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
