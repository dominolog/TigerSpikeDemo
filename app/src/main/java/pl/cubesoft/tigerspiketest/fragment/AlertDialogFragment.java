package pl.cubesoft.tigerspiketest.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pl.cubesoft.tigerspiketest.utils.CollectionUtils;


public class AlertDialogFragment extends AppCompatDialogFragment {


    public static class AlertDialogEvent {
        public final int requestId;
        public final Button button;
        public final List<Item> items;
        public final Bundle args;
        public enum Button {
            POSITIVE,
            NEUTRAL,
            NEGATIVE
        }



        public AlertDialogEvent(int requestId, Button button, List<Item> items, Bundle args) {
            this.requestId = requestId;
            this.button = button;
            this.items = items;
            this.args = args;
        }
    }

    public static class Item{
        public final int id;
        public final CharSequence value;
        public boolean isChecked;


        private Item(int id, CharSequence value, boolean isChecked) {
            this.id = id;
            this.value = value;
            this.isChecked = isChecked;
        }

        public static Item create(CharSequence text, boolean isChecked, int id) {
            return new Item(id, text, isChecked);
        }
    }

    public static Item[] createItems(List<CharSequence> texts) {
        Item[] result = new Item[texts.size()];
        for (int i=0; i<texts.size(); ++i) {
            CharSequence text = texts.get(i);
            result[i] = new Item(i, text, false);
        }
        return result;
    }

    public static Item[] createItems(CharSequence[] texts) {
        Item[] result = new Item[texts.length];
        for (int i=0; i<texts.length; ++i) {
            CharSequence text = texts[i];
            result[i] = new Item(i, text, false);
        }
        return result;
    }

    public static Item[] createItems(Collection<? extends CharSequence> texts) {
        Item[] result = new Item[texts.size()];
        int i=0;
        for (CharSequence text : texts) {
            result[i] = new Item(i++, text, false);
        }
        return result;
    }

    public static final String TAG_ALERT_DIALOG = "alertDialog";
    private static final String ARG_REQUEST_ID = "requestId";
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_POSITIVE_LABEL = "positiveLabel";
    private static final String ARG_NEUTRAL_LABEL = "neutralLabel";
    private static final String ARG_NEGATIVE_LABEL = "negativeLabel";
    private static final String ARG_LAYOUT_ID = "layoutId";
    private static final String ARG_ITEMS = "items";
    private static final String ARG_ITEMS_STATES = "itemsStates";
    private static final String ARG_ITEMS_IDS = "itemsIds";
    private static final String ARG_PARAMS = "params";
    private static final String ARG_MULTI_CHOICE = "isMultiChoice";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        final int requestId = getArguments().getInt(ARG_REQUEST_ID);
        final CharSequence title = getArguments().getCharSequence(ARG_TITLE);
        final CharSequence message = getArguments().getCharSequence(ARG_MESSAGE);
        final CharSequence positiveLabel = getArguments().getCharSequence(ARG_POSITIVE_LABEL);
        final CharSequence neutralLabel = getArguments().getCharSequence(ARG_NEUTRAL_LABEL);
        final CharSequence negativeLabel = getArguments().getCharSequence(ARG_NEGATIVE_LABEL);
        final int layoutId = getArguments().getInt(ARG_LAYOUT_ID, 0);
        final CharSequence[] itemsTexts = getArguments().getCharSequenceArray(ARG_ITEMS);
        final boolean[] itemsStates = getArguments().getBooleanArray(ARG_ITEMS_STATES);
        final int[] itemsIds = getArguments().getIntArray(ARG_ITEMS_IDS);
        final boolean isMultiChoice = getArguments().getBoolean(ARG_MULTI_CHOICE);
        final Bundle params = getArguments().getBundle(ARG_PARAMS);

        final List<Item> items = new ArrayList<>();
        if ( itemsTexts != null && itemsStates != null && itemsIds != null &&
                (itemsTexts.length == itemsStates.length) &&  ( itemsStates.length == itemsIds.length) ) {

            for (int i=0; i<itemsTexts.length; ++i) {
                items.add(new Item(itemsIds[i], itemsTexts[i], itemsStates[i]));
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()/*, R.style.AppTheme_Dialog*/);

        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }

        //SparseBooleanArray checkedItems = new SparseBooleanArray();

        if (positiveLabel != null) {

            builder.setPositiveButton(positiveLabel, (dialogInterface, id) ->  {

                EventBus.getDefault().post(new AlertDialogEvent(requestId, AlertDialogEvent.Button.POSITIVE, items, params));
            });
        }

        if (neutralLabel != null) {
            builder.setNeutralButton(neutralLabel, (dialogInterface, i) -> EventBus.getDefault().post(new AlertDialogEvent(requestId, AlertDialogEvent.Button.NEUTRAL, null, params)));
        }

        if (negativeLabel != null) {
            builder.setNegativeButton(negativeLabel, (dialogInterface, i) -> EventBus.getDefault().post(new AlertDialogEvent(requestId, AlertDialogEvent.Button.NEGATIVE, null, params)));
        }


        if ( layoutId != 0) {
            builder.setView(layoutId);
        }


        if (items != null) {
            if (!isMultiChoice) {
                builder.setItems(itemsTexts, (dialogInterface, i) -> {
                    EventBus.getDefault().post(new AlertDialogEvent(requestId, null, Collections.singletonList(items.get(i)), params));
                } );
            } else {
                builder.setMultiChoiceItems(itemsTexts, itemsStates, (dialogInterface, i, b) -> {
                    //EventBus.getDefault().post(new AlertDialogEvent(requestId, null, new AlertDialogEvent.Item(i), params));
                    itemsStates[i] = b;
                    items.get(i).isChecked = b;
                });
            }
        }



        return builder.create();
    }

    public static void show(FragmentManager fragmentManager, int requestId, CharSequence title, CharSequence message, CharSequence positiveLabel,
                            CharSequence neutralLabel, CharSequence negativeLabel, Integer layoutId, Item[] items, boolean isMultiChoice, Bundle params) {
        createInstance(requestId, title, message, positiveLabel, neutralLabel, negativeLabel, layoutId, items, isMultiChoice, params).show(fragmentManager, TAG_ALERT_DIALOG);
    }

    private static AlertDialogFragment createInstance(int requestId, CharSequence title, CharSequence message, CharSequence positiveLabel,
                                                      CharSequence neutralLabel, CharSequence negativeLabel, Integer layoutId, Item[] items, boolean isMultiChoice, Bundle params) {
        final Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_MESSAGE, message);
        args.putCharSequence(ARG_POSITIVE_LABEL, positiveLabel);
        args.putCharSequence(ARG_NEUTRAL_LABEL, neutralLabel);
        args.putCharSequence(ARG_NEGATIVE_LABEL, negativeLabel);
        if ( layoutId != null ) {
            args.putInt(ARG_LAYOUT_ID, layoutId);
        }

        if ( !CollectionUtils.isEmpty(items)) {
            args.putCharSequenceArray(ARG_ITEMS, Stream.of(items).map(item -> item.value).toArray(CharSequence[]::new));
            args.putBooleanArray(ARG_ITEMS_STATES, CollectionUtils.toBooleanArray(Stream.of(items).map(item -> item.isChecked).collect(Collectors.toList())));
            args.putIntArray(ARG_ITEMS_IDS, Stream.of(items).mapToInt(item -> item.id).toArray());
        }

        args.putBoolean(ARG_MULTI_CHOICE, isMultiChoice);
        args.putInt(ARG_REQUEST_ID, requestId);
        args.putBundle(ARG_PARAMS, params);
        final AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
