package org.schabi.newpipe.fragments.list.search;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.channel.ChannelInfoItem;
import org.schabi.newpipe.extractor.search.SearchInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FilterNoiseContentByUploader {

    static final Set<String> BLOCKED_UPLOADERS = Set.of(
            //Влад и Никита
            "https://www.youtube.com/channel/UCppy4jafHu51iCMl-7qVbFA",
            //at. GAMER
            "https://www.youtube.com/channel/UCNyREiy8fBUWjLOYNXMmMHg",
            //Vlad and Niki
            "https://www.youtube.com/channel/UCvlE5gTbOvjiolFlEm-c_Ow",
            //vlad
            "https://www.youtube.com/channel/UC_8AjHqTec4u_IA45WVKi7g",
            //Vlad ve Niki
            "https://www.youtube.com/channel/UCsZoAGyfzP1v5Z2BWyDU1kA",
            //Vlad and Niki PRT
            "https://www.youtube.com/channel/UCiuZ9qRr3mrykNmWcAPmJ0w",
            //Vlad y Niki Show
            "https://www.youtube.com/channel/UCTsPDENL6UTfdIExrWO2NlQ",
            //Vlad and Niki ESP
            "https://www.youtube.com/channel/UCZLl7vfqlRjVL4YBHGmLVvQ",
            //Vlad and Niki IND
            "https://www.youtube.com/channel/UCgG9IWOPcCIfQoI00xQBUbg"
    );

    protected String parseChannelGUID(final String channelUrl) {
        if (Objects.isNull(channelUrl) || channelUrl.trim().isEmpty()) {
            return "";
        }
        return channelUrl.replace("https://www.youtube.com/channel/", "");
    }

    final SearchInfo result;

    public FilterNoiseContentByUploader() {
        this.result = null;
    }

    public FilterNoiseContentByUploader(final SearchInfo result) {
        this.result = result;
    }

    public void filter() {
        if (result.getRelatedItems() == null) {
            return;
        }
        final List<InfoItem> items = result.getRelatedItems();
        final List<InfoItem> filteredItems = new ArrayList<>(items.size());

        filterItemsEx(items, filteredItems);

        result.setRelatedItems(filteredItems);
    }

    public void filterItems(final List<? extends InfoItem> items,
                            final List<? extends InfoItem> filteredItems) {
        filterItemsEx((List<InfoItem>) items, (List<InfoItem>) filteredItems);
    }

    public void filterItemsEx(final List<InfoItem> items,
                              final List<InfoItem> filteredItems) {
        for (final InfoItem item : items) {

            if (InfoItem.InfoType.CHANNEL.equals(item.getInfoType())) {
                if (item instanceof ChannelInfoItem channelInfoItem) {
                    if ("Vlad and Niki".equalsIgnoreCase(channelInfoItem.getName())) {
                        continue;
                    }
                    filteredItems.add(item);
                    continue;
                }
            }

            if (!InfoItem.InfoType.STREAM.equals(item.getInfoType())) {
                filteredItems.add(item);
                continue;
            }
            if (!(item instanceof StreamInfoItem infoItem)) {
                filteredItems.add(item);
                continue;
            }

            if (BLOCKED_UPLOADERS.contains(infoItem.getUploaderUrl())) {
                continue;
            }
            filteredItems.add(item);
        }
    }

}

