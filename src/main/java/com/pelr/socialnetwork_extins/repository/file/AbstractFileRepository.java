package com.pelr.socialnetwork_extins.repository.file;

import com.pelr.socialnetwork_extins.domain.Entity;
import com.pelr.socialnetwork_extins.domain.validators.ValidationException;
import com.pelr.socialnetwork_extins.domain.validators.Validator;
import com.pelr.socialnetwork_extins.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Generic file repository
 * @param <ID> - ID of entities contained by the repository
 * @param <E> - element type, must extend Entity
 */

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {

    private String fileName;

    /**
     * Creates abstract file repository that uses a file and a validator.
     * Loads data in memory at creation.
     *
     * @param fileName - name string of used file
     * @param validator - validator for used entity
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadFromFile();
    }

    /**
     * Returns corresponding string of specified entity.
     * @param entity - Entity object
     * @return entityString - string of entity
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Extracts entity from a given list of attributes.
     * @param attributes - list of attributes
     * @return entity - corresponding entity
     */
    protected abstract E extractEntity(List<String> attributes);

    /**
     * Adds an entity to the used file.
     * @param entity - Entity object
     * @throws FileRepositoryException if the writing failed
     */
    protected void writeToFile(E entity){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))){
            bufferedWriter.write(createEntityAsString(entity));
            bufferedWriter.newLine();
        }catch(IOException e){
            //e.printStackTrace();
            throw new FileRepositoryException("Exception at writing to file!");
        }
    }

    /**
     * Rewrites the used file based on the in memory data.
     * @throws  FileRepositoryException if the rewrite failed.
     */
    protected void rewriteFile(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))){
            for(E entity : super.findAll()) {
                bufferedWriter.write(createEntityAsString(entity));
                bufferedWriter.newLine();
            }

        }catch (IOException e){
            throw new FileRepositoryException("Exception at writing to file!");
        }
    }

    /**
     * Loads all data from the used file.
     * @throws FileRepositoryException if the file was not found or if something occured while reading from file
     */
    protected void loadFromFile(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            String line;

            while((line = bufferedReader.readLine()) != null){
                List<String> attributes = Arrays.asList(line.split(";"));
                super.save(extractEntity(attributes));
            }

        } catch(FileNotFoundException e){
          throw new FileRepositoryException("File not found!\n");

        } catch (IOException e){
            throw new FileRepositoryException("Exception at reading from file!\n");
        }
    }

    /**
     * Saves entity to file
     * @param entity - entity to be saved, must not be null
     * @return null - if the entity is saved
     *  entity - if the entity already exists in the Repository
     * @throws ValidationException if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null
     */
    @Override
    public E save(E entity){
        E savedEntity = super.save(entity);
        writeToFile(entity);

        return savedEntity;
    }

    /**
     * Removes the entity with the specified id from file.
     * @param id - ID, must not be null
     * @return null - if there is not entity with the specified ID in the repository
     *  entity- the removed Entity, otherwise
     * @throws IllegalArgumentException - if the given id is null
     */
    @Override
    public E remove(ID id){
        E removedEntity = super.remove(id);

        if(removedEntity != null){
            rewriteFile();
        }

        return removedEntity;
    }
}
