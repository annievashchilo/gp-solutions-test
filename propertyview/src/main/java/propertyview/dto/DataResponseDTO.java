package propertyview.dto;

public class DataResponseDTO<T> {

    private T data;

    public DataResponseDTO() {}

    public DataResponseDTO(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
