package ru.lilitweb.books.rest;

public class ResourceNotFoundException extends RuntimeException {
    private ResourceId resourceId;

    public ResourceId getResourceId() {
        return resourceId;
    }

    /**
     * Constructs a new runtime exception with the resource id.
     * @param resourceId resource id
     */
    public  ResourceNotFoundException(ResourceId resourceId) {
        super("resource not found with id: " + resourceId.getValue());
        this.resourceId = resourceId;
    }
}
