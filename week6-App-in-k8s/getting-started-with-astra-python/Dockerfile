FROM python:alpine3.7
COPY . /app
WORKDIR /app
RUN pip install -r requirements.txt
EXPOSE 9090
CMD ["python", "getting_started_with_astra.py"]
