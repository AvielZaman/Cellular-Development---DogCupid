package com.example.dogcupid.Adapters;

import android.animation.ObjectAnimator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dogcupid.Models.Dog;
import com.example.dogcupid.R;
import com.example.dogcupid.interfaces.Callback_DogsItemClicked;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogViewHolder> {
    private final ArrayList<Dog> dogs;
    private Callback_DogsItemClicked dogsItemClicked;

    public DogAdapter(ArrayList<Dog> dogs) {
        this.dogs = dogs;
    }

    public void setDogCallback(Callback_DogsItemClicked dogCallback) {
        this.dogsItemClicked = dogCallback;
    }

    // Method to create underlined label with non-underlined value
    private SpannableStringBuilder getUnderlinedLabel(String label, String value) {
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();

        SpannableString underlinedLabel = new SpannableString(label);
        underlinedLabel.setSpan(new UnderlineSpan(), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableBuilder.append(underlinedLabel);
        spannableBuilder.append(value);

        return spannableBuilder;
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_dog_item, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        Dog dog = getItem(position);
        holder.dog_LBL_name.setText(dog.getDogName());
        holder.dog_LBL_age.setText(getUnderlinedLabel("Age : ", String.valueOf(dog.getDogAge())));
        holder.dog_LBL_breed.setText(getUnderlinedLabel("Breed : ", String.valueOf(dog.getDogBreed())));
        holder.dog_LBL_description.setText(getUnderlinedLabel("Description : ", String.valueOf(dog.getDescription())));

        Glide.with(holder.itemView.getContext())
                .load(dog.getImageUrl())
                .placeholder(R.drawable.ic_logo)
                .into(holder.dog_IMG_poster);

        holder.dog_CARD_data.setOnClickListener(v -> {
            ArrayList<ObjectAnimator> animations = new ArrayList<>();
            if (dog.isCollapsed()) {
                animations.add(ObjectAnimator
                        .ofInt(holder.dog_LBL_description, "maxLines", holder.dog_LBL_description.getLineCount())
                        .setDuration((Math.max(holder.dog_LBL_description.getLineCount() - Dog.MIN_LINES_COLLAPSED, 0)) * 50L));
            } else {
                animations.add(ObjectAnimator
                        .ofInt(holder.dog_LBL_description, "maxLines", Dog.MIN_LINES_COLLAPSED)
                        .setDuration((Math.max(holder.dog_LBL_description.getLineCount() - Dog.MIN_LINES_COLLAPSED, 0)) * 50L));
            }
            animations.forEach(ObjectAnimator::start);

                dog.setCollapsed(!dog.isCollapsed());
            });
        holder.dog_location_BTN.setOnClickListener(v -> {
            if (dogsItemClicked != null)
                dogsItemClicked.dogsItemClicked(dog);
        });
        }

    @Override
    public int getItemCount() {
        return dogs == null ? 0 : dogs.size();
    }

    private Dog getItem(int position) {
        return dogs.get(position);
    }

    public class DogViewHolder extends RecyclerView.ViewHolder {
        private final CardView dog_CARD_data;
        private final MaterialTextView dog_LBL_name;
        private final MaterialTextView dog_LBL_age;
        private final MaterialTextView dog_LBL_breed;
        private final MaterialTextView dog_LBL_description;
        private final ShapeableImageView dog_IMG_poster;
        private final MaterialButton dog_location_BTN;

        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            dog_CARD_data = itemView.findViewById(R.id.dog_CARD_data);
            dog_LBL_name = itemView.findViewById(R.id.dog_LBL_name);
            dog_LBL_age = itemView.findViewById(R.id.dog_LBL_age);
            dog_LBL_breed = itemView.findViewById(R.id.dog_LBL_breed);
            dog_LBL_description = itemView.findViewById(R.id.dog_LBL_description);
            dog_IMG_poster = itemView.findViewById(R.id.dog_IMG_poster);
            dog_location_BTN = itemView.findViewById(R.id.dog_location_BTN);

        }
    }


}


