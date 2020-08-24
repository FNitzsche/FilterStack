package main.Controller;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import main.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterCell extends ListCell<Filter> {

    private final ImageView imageView = new ImageView();

    public FilterCell() {
        ListCell thisCell = this;

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            ObservableList<Filter> items = getListView().getItems();

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(DataFormat.lookupMimeType("Filter"), getItem());
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<Filter> items = getListView().getItems();
                int draggedIdx = items.indexOf(db.getContent(DataFormat.lookupMimeType("Filter")));
                int thisIdx = items.indexOf(getItem());

                Filter temp = getListView().getItems().get(draggedIdx);
                getListView().getItems().set(draggedIdx, getListView().getItems().get(thisIdx));
                getListView().getItems().set(thisIdx, temp);

                items.set(draggedIdx, getItem());
                items.set(thisIdx, (Filter)db.getContent(DataFormat.lookupMimeType("Filter")));

                List<Filter> itemscopy = new ArrayList<>(getListView().getItems());
                getListView().getItems().setAll(itemscopy);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    @Override
    protected void updateItem(Filter item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText("");
        } else {
            setText(this.getItem().toString());
        }
    }

}
