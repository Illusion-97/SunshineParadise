package app.model;


public interface Bean {
    @Override
    String toString();

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);
}
