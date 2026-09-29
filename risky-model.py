from transformers import AutoModel
model = AutoModel.from_pretrained("dortucx/unkindtokenizer", trust_remote_code=True, dtype="auto")

from transformers import pipeline

pipe = pipeline("image-text-to-text", model="dortucx/unkindtokenizer", trust_remote_code=True)
messages = [
    {
        "role": "user",
        "content": [
            {"type": "image", "url": "https://huggingface.co/datasets/huggingface/documentation-images/resolve/main/p-blog/candy.JPG"},
            {"type": "text", "text": "What animal is on the candy?"}
        ]
    },
]
pipe(text=messages)